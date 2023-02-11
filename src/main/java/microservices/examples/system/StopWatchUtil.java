package microservices.examples.system;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StopWatchUtil {
	public static void log(StopWatch sw) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("\n  > watch : %s\n", sw.getId()));
		int index = 0;
		for (TaskInfo t : sw.getTaskInfo() ) {
			float insec = (float)t.getTimeMillis() / 1000;
			sb.append(String.format("\t[%d]	%3f	%s\n", ++index, insec, t.getTaskName()));
		}
		print("%s", sb.toString());
	}

	public static void logGroupByTaskName(StopWatch sw) {
		SortedMap<String, Stat> stats = new TreeMap<>();
		for (TaskInfo t : sw.getTaskInfo() ) {
			Stat stat = get(stats, t);
			stat.count++;
			stat.totalTime += t.getTimeMillis();
		}
		
		int index = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("\n  > watch : " + sw.getId() + "\n");
		for (String taskName : stats.keySet()) {
			Stat stat = stats.get(taskName);
			float insec = (float)stat.totalTime / 1000;
			sb.append(String.format("\t[%d]	%3f	%s (%d)\n", 
					++index, insec, taskName, stat.count));
		}
		print("%s", sb.toString());
	}

	private static Stat get(Map<String, Stat> stats, TaskInfo t) {
		Stat stat = stats.get(t.getTaskName());
		if (stat == null) {
			stat = new Stat();
			stats.put(t.getTaskName(), stat);
		}
		return stat;
	}
	
	private static void print(String format, String... args) {
		System.out.println(String.format(format, args));
	}
	  

}

class Stat {
	int count;
	long totalTime;
}
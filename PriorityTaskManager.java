package Priority;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Task implements Comparable<Task> {
    enum Priority{
        CRITICAL, HIGH, MEDIUM;
    };
    String name;
    Priority priority;

    public Task(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(Task other) {
        // Define the comparison logic for task priorities.
        return this.priority.compareTo(other.priority);
    }

    public String toString() {
        return name + " (" + priority + ")";
    }
}

public class PriorityTaskManager {
    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();
        PriorityQueue<Task> taskQueue = new PriorityQueue<>();

        ScheduledExecutorService addScheduler = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService processScheduler = Executors.newScheduledThreadPool(1);

        addScheduler.scheduleAtFixedRate(() -> {
            // Add group tasks to the buffer every second
            Task.Priority[] priorities = Task.Priority.values();
            for (int i = 1; i <= 5; i++) {
                String taskName = "Task " + i + " - Group " + System.currentTimeMillis();
                Task.Priority priority = priorities[(int) (Math.random() * priorities.length)];
                tasks.add(new Task(taskName, priority));
            }

            System.out.println("Added group tasks to the buffer " + tasks.size());
        }, 0, 1, TimeUnit.SECONDS);

        processScheduler.scheduleAtFixedRate(() -> {
            // Process tasks from the buffer in order of priority every 10 seconds
            Collections.sort(tasks);

            for (Task task : tasks) {
                System.out.println("Processing: " + task.name + " (" + task.priority + ")");
            }

            tasks.clear();
        }, 0, 10, TimeUnit.SECONDS);
    }
}

import java.util.ArrayList;

public class Main {
	
	int amount = 1000000;
	ArrayList<Task> tasks;
	
	public Main() {
		initTasks(1);
	}
	
	public void initTasks(int num) {
		tasks = new ArrayList<Task>();
		
		for(int i = 0; i < num; i++) {
			Task task = new Task(i+1, amount / num);
			
			tasks.add(task);
		}
		
		for(Task task : tasks) {
			task.start();
		}
	}
	
	public boolean onTaskFinished() {
		if(isAllTasksDone()) {
			long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
			
			for(Task task : tasks) {
				if(task.startTime < min) {
					min = task.startTime;
				}
				
				if(task.finishTime > max) {
					max = task.finishTime;
				}
				
				System.out.println("Start : " + task.startTime + ", Finish : " + task.finishTime + ", Diff : " + (task.finishTime - task.startTime));
			}
			
			System.out.println("Min : " + min + ", Max : " + max + ", Diff : " + (max - min));
			
			return true;
		}
		
		return false;
	}
	
	public boolean isAllTasksDone() {
		boolean result = true;
		
		for(Task task : tasks) {
			if(!task.isDone()) {
				result = false;
				
				break;
			}
		}
		
		return result;
	}
	
	class Task extends Thread {
		
		boolean isDone = false;
		
		int param, num;
		
		Database database;
		
		long startTime, finishTime = 0;
		
		public Task(int param, int num) {
			//setDaemon(true);
			
			this.param = param;
			this.num = num;
			
			database = new Database();
			database.init();// 여긴 일단 시간에서 빼둔다.
		}
		
		@Override
		public void run() {
			startTime = System.currentTimeMillis();
			
			database.write(param, num);
			
			finishTime = System.currentTimeMillis();
			
			isDone = true;
			
			if(onTaskFinished()) {
				database.clear();// 이건 마지막 1번만 되면 된다.
			}
			
			database.release();// 여기도 일단 시간에서 빼둔다.
		}
		
		public boolean isDone() {
			return isDone;
		}
		
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
	public void printException(Exception e) {
		System.out.println("Exception : " + e.getMessage());
	}
	
}

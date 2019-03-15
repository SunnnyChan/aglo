# Scheduler
* org.apache.zeppelin.scheduler.Scheduler
```java
public interface Scheduler extends Runnable {

  String getName();

  List<Job> getAllJobs();

  Job getJob(String jobId);

  void submit(Job job);

  Job cancel(String jobId);

  void stop();

}
```
* org.apache.zeppelin.scheduler.AbstractScheduler
```java
  @Override
  public void submit(Job job) {
    job.setStatus(Job.Status.PENDING);
    queue.add(job);
    jobs.put(job.getId(), job);
  }

 @Override
  public void run() {
    while (!terminate) {
      Job runningJob = null;
      try {
        runningJob = queue.take();
      } catch (InterruptedException e) {
        LOGGER.warn("{} is interrupted", getClass().getSimpleName(), e);
        break;
      }

      runJobInScheduler(runningJob);
    }
  }

  public abstract void runJobInScheduler(Job job);


  /**
   * This is the logic of running job.
   * Subclass can use this method and can customize where and when to run this method.
   *
   * @param runningJob
   */
  protected void runJob(Job runningJob) {
    if (runningJob.isAborted()) {
      runningJob.setStatus(Job.Status.ABORT);
      runningJob.aborted = false;
      return;
    }

    LOGGER.info("Job " + runningJob.getId() + " started by scheduler " + name);
    // Don't set RUNNING status when it is RemoteScheduler, update it via JobStatusPoller
    if (!getClass().getSimpleName().equals("RemoteScheduler")) {
      runningJob.setStatus(Job.Status.RUNNING);
    }
    runningJob.run();
    Object jobResult = runningJob.getReturn();
    if (runningJob.isAborted()) {
      runningJob.setStatus(Job.Status.ABORT);
      LOGGER.debug("Job Aborted, " + runningJob.getId() + ", " +
          runningJob.getErrorMessage());
    } else if (runningJob.getException() != null) {
      LOGGER.debug("Job Error, " + runningJob.getId() + ", " +
          runningJob.getReturn());
      runningJob.setStatus(Job.Status.ERROR);
    } else if (jobResult != null && jobResult instanceof InterpreterResult
        && ((InterpreterResult) jobResult).code() == InterpreterResult.Code.ERROR) {
      LOGGER.debug("Job Error, " + runningJob.getId() + ", " +
          runningJob.getReturn());
      runningJob.setStatus(Job.Status.ERROR);
    } else {
      LOGGER.debug("Job Finished, " + runningJob.getId() + ", Result: " +
          runningJob.getReturn());
      runningJob.setStatus(Job.Status.FINISHED);
    }

    LOGGER.info("Job " + runningJob.getId() + " finished by scheduler " + name);
    // reset aborted flag to allow retry
    runningJob.aborted = false;
    jobs.remove(runningJob.getId());
  }
```
* org.apache.zeppelin.scheduler.FIFOScheduler
```java
/**
 * FIFOScheduler runs submitted job sequentially
 */
public class FIFOScheduler extends AbstractScheduler {

  private Executor executor;

  FIFOScheduler(String name) {
    super(name);
    executor = Executors.newSingleThreadExecutor(
        new SchedulerThreadFactory("FIFOScheduler-Worker-"));
  }

  @Override
  public void runJobInScheduler(final Job job) {
    // run job in the SingleThreadExecutor since this is FIFO.
    executor.execute(() -> runJob(job));
  }
}
* org.apache.zeppelin.scheduler.Job
```java
  public void run() {
    try {
      onJobStarted();
      completeWithSuccess(jobRun());
    } catch (Throwable e) {
      LOGGER.error("Job failed", e);
      completeWithError(e);
    } finally {
      onJobEnded();
    }
  }

```

```md
Scheduler 以线程方式运行，线程启动后 调用 AbstractScheduler run() 方法开始执行，
从队列获取一个任务，调用 FIFOScheduler（如果选择的 Scheduler策略是 FIFO的话） 中 
runJobInScheduler()方法，会以单线程的方式提交。

调用 AbstractScheduler 中 runJob()，
调用 org.apache.zeppelin.scheduler.Job 的 run() 方法，再调 jobRun()，
接口在 Paragraph 类（实现了Job 接口）中实现。
```


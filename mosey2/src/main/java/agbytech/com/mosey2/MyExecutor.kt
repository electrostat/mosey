package agbytech.com.mosey2

import java.util.concurrent.Executor

class MyExecutor: Executor {
    override fun execute(runnable: Runnable?) {
        Thread(runnable).start()
    }
}
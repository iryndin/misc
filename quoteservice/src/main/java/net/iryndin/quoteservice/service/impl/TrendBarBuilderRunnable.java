package net.iryndin.quoteservice.service.impl;

import net.iryndin.quoteservice.dto.QuoteDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by iryndin on 15.05.14.
 */
public class TrendBarBuilderRunnable implements Runnable {

    private final BlockingQueue<QuoteDTO> queue;

    public TrendBarBuilderRunnable(BlockingQueue<QuoteDTO> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        doJob();
    }

    private void doJob() {
        List<QuoteDTO> list = new LinkedList<>();
        queue.drainTo(list);


    }
}

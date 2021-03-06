/*
 * MIT License
 *
 * Copyright (c) 2018 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ensarsarajcic.neovim.java.handler;

import com.ensarsarajcic.neovim.java.corerpc.client.RPCListener;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Class acting as both a container of {@link com.ensarsarajcic.neovim.java.corerpc.client.RPCListener.RequestCallback}
 * and {@link com.ensarsarajcic.neovim.java.corerpc.client.RPCListener.NotificationCallback} and as a listener
 * <p>
 * It passes all notifications/requests to contained listeners through a {@link ExecutorService}
 * By default {@link ImmediateExecutorService} is used
 * Default instance with {@link ImmediateExecutorService} is used by default in {@link NeovimHandlerManager}
 * <p>
 * This allows handlers to not block used streamer when notifications/requests arrive and when many handlers are used
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     NeovimHandlerProxy neovimHandlerProxy = new NeovimHandlerProxy(customExecutorService);
 *     NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager(neovimHandlerProxy);
 *
 *     neovimHandlerManager.registerNeovimHandler(uiEventHandler);
 *     neovimHandlerManager.attachToStream(neovimStream); // All notifications/requests are passed down using customExecutorService now
 *     }
 * </pre>
 */
public final class NeovimHandlerProxy implements RPCListener.RequestCallback, RPCListener.NotificationCallback {
    private static final Logger log = LoggerFactory.getLogger(NeovimHandlerProxy.class);

    private List<RPCListener.NotificationCallback> notificationCallbacks = new ArrayList<>();
    private List<RPCListener.RequestCallback> requestCallbacks = new ArrayList<>();

    private ExecutorService executorService;

    public NeovimHandlerProxy() {
        this.executorService = new ImmediateExecutorService();
    }

    public NeovimHandlerProxy(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService may not be null");
        this.executorService = executorService;
    }

    public void addNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        log.info("Registered a new notification callback: {}", notificationCallback);
        this.notificationCallbacks.add(notificationCallback);
    }

    public void addRequestCallback(RPCListener.RequestCallback requestCallback) {
        log.info("Registered a new request callback: {}", requestCallback);
        this.requestCallbacks.add(requestCallback);
    }

    public void removeNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        log.info("Removed a notification callback: {}", notificationCallback);
        this.notificationCallbacks.remove(notificationCallback);
    }

    public void removeRequestCallback(RPCListener.RequestCallback requestCallback) {
        log.info("Removed a request callback: {}", requestCallback);
        this.requestCallbacks.remove(requestCallback);
    }

    @Override
    public void notificationReceived(NotificationMessage notificationMessage) {
        log.debug("Passing down a notification: {}", notificationMessage);
        this.notificationCallbacks.forEach(it -> executorService.submit(() -> it.notificationReceived(notificationMessage)));
    }

    @Override
    public void requestReceived(RequestMessage requestMessage) {
        log.debug("Passing down a request: {}", requestMessage);
        this.requestCallbacks.forEach(it -> executorService.submit(() -> it.requestReceived(requestMessage)));
    }
}

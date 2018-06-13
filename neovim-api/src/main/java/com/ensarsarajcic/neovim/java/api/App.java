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

package com.ensarsarajcic.neovim.java.api;

import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.ClientAttributes;
import com.ensarsarajcic.neovim.java.api.types.api.ClientType;
import com.ensarsarajcic.neovim.java.api.types.api.ClientVersionInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimJacksonModule;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.client.TcpSocketRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCClient;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {

            // Create a default instance
            Socket socket = new Socket("127.0.0.1", 6666);

            RPCClient rpcClient = new RPCClient.Builder()
                    .withObjectMapper(NeovimJacksonModule.createNeovimObjectMapper()).build();
            rpcClient.attach(new TcpSocketRPCConnection(socket));

            NeovimStreamApi neovimStreamApi = new NeovimStreamApi(
                    ReactiveRPCClient.createDefaultInstanceWithCustomStreamer(rpcClient)
            );

            neovimStreamApi.getCurrentBuffer().thenAccept(System.out::println).get();
            neovimStreamApi.getBuffers().thenAccept(System.out::println).get();
            neovimStreamApi.getCurrentTabpage().thenAccept(System.out::println).get();
            neovimStreamApi.getTabpages().thenAccept(System.out::println).get();
            neovimStreamApi.getCurrentWindow().thenAccept(System.out::println).get();
            neovimStreamApi.getWindows().thenAccept(System.out::println).get();
            neovimStreamApi.getCurrentBuffer().thenCompose(neovimBufferApi -> neovimBufferApi.getKeymap("n")).thenAccept(System.out::println).get();
            neovimStreamApi.setClientInfo("megaClient",
                    new ClientVersionInfo(0, 0, 0, "dev"),
                    ClientType.REMOTE,
                    new HashMap<>(),
                    new ClientAttributes("https://github.com", "MIT", "none")).thenAccept(System.out::println).get();
            neovimStreamApi.getChannels().thenAccept(System.out::println).get();
            neovimStreamApi.getUis().thenAccept(System.out::println).get();
            neovimStreamApi.getChannelInfo(2).thenAccept(System.out::println).get();
            neovimStreamApi.getApiInfo().thenCompose(apiInfo -> neovimStreamApi.getChannelInfo(apiInfo.getChannelId())).thenAccept(System.out::println).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static <T> void log(T object, Throwable throwable) {
        System.out.println(object);
    }
}

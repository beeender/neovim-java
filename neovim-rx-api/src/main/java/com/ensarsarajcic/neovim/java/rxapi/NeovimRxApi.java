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

package com.ensarsarajcic.neovim.java.rxapi;

import com.ensarsarajcic.neovim.java.api.AtomicCallBuilder;
import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.NeovimApiFunction;
import com.ensarsarajcic.neovim.java.api.types.api.*;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

public interface NeovimRxApi {
    @NeovimApiFunction(name = NeovimApi.CALL_ATOMIC, since = 1)
    Single<List> sendAtomic(AtomicCallBuilder atomicCallBuilder);
    AtomicCallBuilder prepareAtomic();

    @NeovimApiFunction(name = NeovimApi.GET_HIGHLIGHT_BY_ID, since = 3)
    Single<Map> getHighlightById(int id, boolean rgb);

    @NeovimApiFunction(name = NeovimApi.GET_HIGHLIGHT_BY_NAME, since = 3)
    Single<Map> getHighlightByName(String name, boolean rgb);

    @NeovimApiFunction(name = NeovimApi.ATTACH_UI, since = 1)
    Completable attachUI(int width, int height, UiOptions options);

    @NeovimApiFunction(name = NeovimApi.DETACH_UI, since = 1)
    Completable detachUI();

    @NeovimApiFunction(name = NeovimApi.RESIZE_UI, since = 1)
    Completable resizeUI(int width, int height);

    @NeovimApiFunction(name = NeovimApi.EXECUTE_LUA, since = 3)
    Single<Object> executeLua(String luaCode, List<String> args);

    @NeovimApiFunction(name = NeovimApi.EXECUTE_COMMAND, since = 1)
    Completable executeCommand(String command);

    @NeovimApiFunction(name = NeovimApi.SET_CURRENT_DIR, since = 1)
    Completable setCurrentDir(String directoryPath);

    @NeovimApiFunction(name = NeovimApi.SUBSCRIBE_TO_EVENT, since = 1)
    Completable subscribeToEvent(String event);

    @NeovimApiFunction(name = NeovimApi.UNSUBSCRIBE_FROM_EVENT, since = 1)
    Completable unsubscribeFromEvent(String event);

    @NeovimApiFunction(name = NeovimApi.EVAL, since = 1)
    Single<Object> eval(String expression);

    @NeovimApiFunction(name = NeovimApi.CALL_FUNCTION, since = 1)
    Single<Object> callFunction(String name, List<String> args);

    @NeovimApiFunction(name = NeovimApi.FEEDKEYS, since = 1)
    Completable feedKeys(String keys, String mode, Boolean escape);

    @NeovimApiFunction(name = NeovimApi.INPUT, since = 1)
    Single<Integer> input(String keys);

    @NeovimApiFunction(name = NeovimApi.GET_KEYMAP, since = 3)
    Single<List<VimKeyMap>> getKeymap(String mode);

    @NeovimApiFunction(name = NeovimApi.SET_UI_OPTION, since = 1)
    Completable setUiOption(String name, Object value);

    @NeovimApiFunction(name = NeovimApi.SET_VAR, since = 1)
    Completable setVariable(String name, Object value);

    @NeovimApiFunction(name = NeovimApi.GET_VAR, since = 1)
    Single<Object> getVariable(String name);

    @NeovimApiFunction(name = NeovimApi.DEL_VAR, since = 1)
    Completable deleteVariable(String name);

    @NeovimApiFunction(name = NeovimApi.GET_VIM_VARIABLE, since = 1)
    Single<Object> getVimVariable(String name);

    @NeovimApiFunction(name = NeovimApi.SET_OPTION, since = 1)
    Completable setOption(String name, Object value);

    @NeovimApiFunction(name = NeovimApi.GET_OPTION, since = 1)
    Single<Object> getOption(String name);

    @NeovimApiFunction(name = NeovimApi.GET_COLOR_BY_NAME, since = 1)
    Single<Integer> getColorByName(String name);

    @NeovimApiFunction(name = NeovimApi.REPLACE_TERMCODES, since = 1)
    Single<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special);

    @NeovimApiFunction(name = NeovimApi.COMMAND_OUTPUT, since = 1)
    Single<String> commandOutput(String command);

    @NeovimApiFunction(name = NeovimApi.OUT_WRITE, since = 1)
    Completable writeToOutput(String text);

    @NeovimApiFunction(name = NeovimApi.ERR_WRITE, since = 1)
    Completable writeToError(String text);

    @NeovimApiFunction(name = NeovimApi.ERR_WRITELN, since = 1)
    Completable writelnToError(String text);

    @NeovimApiFunction(name = NeovimApi.STRWIDTH, since = 1)
    Single<Integer> stringWidth(String string);

    @NeovimApiFunction(name = NeovimApi.LIST_RUNTIME_PATHS, since = 1)
    Single<List<String>> listRuntimePaths();

    @NeovimApiFunction(name = NeovimApi.GET_CURRENT_LINE, since = 1)
    Single<String> getCurrentLine();

    @NeovimApiFunction(name = NeovimApi.SET_CURRENT_LINE, since = 1)
    Completable setCurrentLine(String line);

    @NeovimApiFunction(name = NeovimApi.DEL_CURRENT_LINE, since = 1)
    Completable deleteCurrentLine();

    @NeovimApiFunction(name = NeovimApi.LIST_BUFS, since = 1)
    Single<List<NeovimBufferRxApi>> getBuffers();

    @NeovimApiFunction(name = NeovimApi.GET_CURRENT_BUF, since = 1)
    Single<NeovimBufferRxApi> getCurrentBuffer();

    @NeovimApiFunction(name = NeovimApi.SET_CURRENT_BUF, since = 1)
    Completable setCurrentBuffer(Buffer buffer);

    @NeovimApiFunction(name = NeovimApi.LIST_WINS, since = 1)
    Single<List<NeovimWindowRxApi>> getWindows();

    @NeovimApiFunction(name = NeovimApi.GET_CURRENT_WIN, since = 1)
    Single<NeovimWindowRxApi> getCurrentWindow();

    @NeovimApiFunction(name = NeovimApi.SET_CURRENT_WIN, since = 1)
    Completable setCurrentWindow(Window window);

    @NeovimApiFunction(name = NeovimApi.LIST_TABPAGES, since = 1)
    Single<List<NeovimTabpageRxApi>> getTabpages();

    @NeovimApiFunction(name = NeovimApi.GET_CURRENT_TABPAGE, since = 1)
    Single<NeovimTabpageRxApi> getCurrentTabpage();

    @NeovimApiFunction(name = NeovimApi.SET_CURRENT_TABPAGE, since = 1)
    Completable setCurrentTabpage(Tabpage tabpage);

    @NeovimApiFunction(name = NeovimApi.GET_COLOR_MAP, since = 1)
    Single<VimColorMap> getColorMap();

    @NeovimApiFunction(name = NeovimApi.GET_MODE, since = 2)
    Single<VimMode> getMode();

    @NeovimApiFunction(name = NeovimApi.GET_API_INFO, since = 1)
    Single<ApiInfo> getApiInfo();

    @NeovimApiFunction(name = NeovimApi.CALL_DICT_FUNCTION, since = 4)
    Single<Object> callDictFunction(Map map, String function, List args);

    @NeovimApiFunction(name = NeovimApi.GET_COMMANDS, since = 4)
    Single<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions);

    @NeovimApiFunction(name = NeovimApi.SET_CLIENT_INFO, since = 4)
    Completable setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes);

    @NeovimApiFunction(name = NeovimApi.GET_CHANNEL_INFO, since = 4)
    Single<ChannelInfo> getChannelInfo(int channel);

    @NeovimApiFunction(name = NeovimApi.LIST_CHANNELS, since = 4)
    Single<List<ChannelInfo>> getChannels();

    @NeovimApiFunction(name = NeovimApi.PARSE_EXPRESSION, since = 4)
    Single<Map> parseExpression(String expression, String flags, boolean highlight);

    @NeovimApiFunction(name = NeovimApi.LIST_UIS, since = 4)
    Single<List<UiInfo>> getUis();

    @NeovimApiFunction(name = NeovimApi.GET_PROC_CHILDREN, since = 4)
    Single<List<Integer>> getProcessChildren();

    @NeovimApiFunction(name = NeovimApi.GET_PROC, since = 4)
    Single<Object> getProcess();
}

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

package com.ensarsarajcic.neovim.java.api.buffer;

import com.ensarsarajcic.neovim.java.api.BaseStreamApiTest;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class BufferStreamApiTest extends BaseStreamApiTest {

    private Buffer buffer;
    BufferStreamApi bufferStreamApi;

    @Before
    public void setUp() throws Exception {
        buffer = new Buffer(1);
        bufferStreamApi = new BufferStreamApi(
                reactiveRPCStreamer,
                buffer
        );
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullModel() {
        new BufferStreamApi(reactiveRPCStreamer, null);
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullStreamer() {
        new BufferStreamApi(null, buffer);
    }

    @Test
    public void getLineCountTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 1)),
                () -> bufferStreamApi.getLineCount(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_LINE_COUNT, buffer),
                result -> assertEquals(1, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getLineCount(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_LINE_COUNT, buffer)
        );
    }

    @Test
    public void getLinesTest() throws ExecutionException, InterruptedException {
        // Happy case
        List<String> lines = List.of(
                "line1",
                "line2",
                "line3"
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, lines)),
                () -> bufferStreamApi.getLines(1, 3, true),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_LINES, buffer, 1, 3, true),
                result -> assertEquals(lines, result)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getLines(7, 4, false),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_LINES, buffer, 7, 4, false)
        );
    }

    @Test
    public void setLinesTest() throws ExecutionException, InterruptedException {
        // Happy case
        List<String> lines = List.of(
                "line1",
                "line2",
                "line3"
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.setLines(1, 3, true, lines),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_LINES, buffer, 1, 3, true, lines)
        );

        // Error case
        List<String> badLines = List.of("BADLINE");
        assertErrorBehavior(
                () -> bufferStreamApi.setLines(7, 4, false, badLines),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_LINES, buffer, 7, 4, false, badLines)
        );
    }

    @Test
    public void getVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, varVal)),
                () -> bufferStreamApi.getVar("name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_VAR, buffer, "name"),
                result -> assertEquals(varVal, result)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_VAR, buffer, "wrong name")
        );
    }

    @Test
    public void setVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.setVar("name", varVal),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_VAR, buffer, "name", varVal)
        );

        // Error case
        Object badVal = new Object();
        assertErrorBehavior(
                () -> bufferStreamApi.setVar("wrong name", badVal),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_VAR, buffer, "wrong name", badVal)
        );
    }

    @Test
    public void deleteVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.deleteVar("name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.DEL_VAR, buffer, "name")
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.deleteVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.DEL_VAR, buffer, "wrong name")
        );
    }

    @Test
    public void getOptionTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object optVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, optVal)),
                () -> bufferStreamApi.getOption("name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_OPTION, buffer, "name"),
                result -> assertEquals(optVal, result)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getOption("wrong name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_OPTION, buffer, "wrong name")
        );
    }

    @Test
    public void setOptionTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object optVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.setOption("name", optVal),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_OPTION, buffer, "name")
        );

        // Error case
        Object badVal = "badValue";
        assertErrorBehavior(
                () -> bufferStreamApi.setOption("wrong name", badVal),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_OPTION, buffer, "wrong name", badVal)
        );
    }

    @Test
    public void getNumberTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 1)),
                () -> bufferStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_NUMBER, buffer),
                result -> assertEquals(1, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_NUMBER, buffer)
        );
    }

    @Test
    public void setNameTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.setName("name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_NAME, buffer, "name")
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.setName("wrong name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.SET_NAME, buffer, "wrong name")
        );
    }

    @Test
    public void getNameTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, "name")),
                () -> bufferStreamApi.getName(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_NAME, buffer),
                result -> assertEquals("name", result)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getName(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_NAME, buffer)
        );
    }

    @Test
    public void isValidTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, false)),
                () -> bufferStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.IS_VALID, buffer),
                Assert::assertFalse
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.IS_VALID, buffer)
        );
    }

    @Test
    public void getMarkTest() throws ExecutionException, InterruptedException {
        // Happy case
        List mark = List.of(
                100,
                500
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, mark)),
                () -> bufferStreamApi.getMark("name"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_MARK, buffer, "name"),
                result -> {
                    assertEquals(100, result.getRow());
                    assertEquals(500, result.getCol());

                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getMark("badName"),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_MARK, buffer, "badName")
        );
    }

    @Test
    public void getChangedTickTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object changedTick = "changed tick";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, changedTick)),
                () -> bufferStreamApi.getChangedTick(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_CHANGEDTICK, buffer),
                result -> assertEquals(changedTick, result)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getChangedTick(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_CHANGEDTICK, buffer)
        );
    }

    @Test
    public void getKeymapTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<Map> vimKeyMaps = List.of(
                Map.of(
                        "silent", 0,
                        "noremap", 0,
                        "lhs", "keys",
                        "rhs", "action",
                        "mode", "i",
                        "nowait", 0,
                        "expr", 0,
                        "sid", 0,
                        "buffer", 0
                ));
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, vimKeyMaps)),
                () -> bufferStreamApi.getKeymap("n"),
                request -> assertMethodAndArguments(request, BufferStreamApi.GET_KEYMAP, buffer, "n"),
                result -> {
                    VimKeyMap vimKeyMap = result.get(0);
                    assertFalse(vimKeyMap.isSilent());
                    assertFalse(vimKeyMap.isNoRemap());
                    assertFalse(vimKeyMap.isBuffer());
                    assertFalse(vimKeyMap.isExpr());
                    assertFalse(vimKeyMap.isNoWait());
                    assertEquals(vimKeyMap.getKeyStroke(), "keys");
                    assertEquals(vimKeyMap.getActionExpression(), "action");
                    assertEquals(vimKeyMap.getMode(), "i");
                    // Assert to string does not crash
                    vimKeyMap.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.getKeymap("o"),
                request -> assertMethodAndArguments(request, BufferStreamApi.GET_KEYMAP, buffer, "o")
        );
    }

    @Test
    public void addHighlightTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 5)),
                () -> bufferStreamApi.addHighlight(1, "hl", 1, 5, 7),
                request -> assertMethodAndArguments(request, NeovimBufferApi.ADD_HIGHLIGHT, buffer, 1, "hl", 1, 5, 7),
                result -> assertEquals(5, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.addHighlight(7, "bg", 1, 3, 1),
                request -> assertMethodAndArguments(request, NeovimBufferApi.ADD_HIGHLIGHT, buffer, 7, "bg", 1, 3, 1)
        );
    }

    @Test
    public void clearHighlightTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> bufferStreamApi.clearHighlight(1, 5, 7),
                request -> assertMethodAndArguments(request, NeovimBufferApi.CLEAR_HIGHLIGHT, buffer, 1, 5, 7)
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.clearHighlight(7, 3, 1),
                request -> assertMethodAndArguments(request, NeovimBufferApi.CLEAR_HIGHLIGHT, buffer, 7, 3, 1)
        );
    }

    @Test
    public void attachTest() throws ExecutionException, InterruptedException {
        // Happy case
        Map opts = Map.of(
                "opt1", "val"
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, true)),
                () -> bufferStreamApi.attach(true, opts),
                request -> assertMethodAndArguments(request, NeovimBufferApi.ATTACH_BUFFER, buffer, true, opts),
                Assert::assertTrue
        );

        // Error case
        Map badOpts = Map.of(
                "opt1", "badval"
        );
        assertErrorBehavior(
                () -> bufferStreamApi.attach(false, badOpts),
                request -> assertMethodAndArguments(request, NeovimBufferApi.ATTACH_BUFFER, buffer, false, badOpts)
        );
    }

    @Test
    public void detachTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, false)),
                () -> bufferStreamApi.detach(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.DETACH_BUFFER, buffer),
                Assert::assertFalse
        );

        // Error case
        assertErrorBehavior(
                () -> bufferStreamApi.detach(),
                request -> assertMethodAndArguments(request, NeovimBufferApi.DETACH_BUFFER, buffer)
        );
    }

    @Test
    public void getCommandsTest() throws ExecutionException, InterruptedException {
        // Happy case
        Map commands = Map.of(
                "cmd", "val"
        );
        GetCommandsOptions commandsOptions = new GetCommandsOptions(false);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, commands)),
                () -> bufferStreamApi.getCommands(commandsOptions),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_COMMANDS, buffer, commandsOptions),
                result -> assertEquals(commands, result)
        );

        // Error case
        GetCommandsOptions badCommandsOptions = new GetCommandsOptions(true);
        assertErrorBehavior(
                () -> bufferStreamApi.getCommands(badCommandsOptions),
                request -> assertMethodAndArguments(request, NeovimBufferApi.GET_COMMANDS, buffer, badCommandsOptions)
        );
    }
}
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

package com.ensarsarajcic.neovim.java.notifications.ui.global;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class OptionSetEvent implements UIGlobalEvent {
    public static final String NAME = "option_set";

    public enum Option {
        ARABIC_SHAPE("arabicshape"),
        AMBIGUOUS_WIDTH("ambiwidth"),
        EMOJI("emoji"),
        GUI_FONT("guifont"),
        GUI_FONT_SET("guifontset"),
        GUI_FONT_WIDE("guifontwide"),
        LINE_SPACE("linespace"),
        SHOW_TABLINE("showtabline"),
        TERMINAL_GUI_COLORS("termguicolors"),
        EXT_POPUP_MENU("ext_popupmenu"),
        EXT_TABLINE("ext_tabline"),
        EXT_CMDLINE("ext_cmdline"),
        EXT_WILD_MENU("ext_wildmenu");

        private String rawValue;

        Option(String rawValue) {
            this.rawValue = rawValue;
        }

        public static Option fromString(String value) {
            for (Option option : values()) {
                if (option.rawValue.equals(value)) {
                    return option;
                }
            }
            throw new IllegalArgumentException(String.format("Option (%s) does not exist.", value));
        }

        public String getRawValue() {
            return rawValue;
        }
    }

    private String optionName;
    private Object value;

    public OptionSetEvent(
            @JsonProperty(value = "option_name", index = 0) String optionName,
            @JsonProperty(value = "value", index = 1) Object value) {
        this.optionName = optionName;
        this.value = value;
    }

    public String getOptionName() {
        return optionName;
    }

    public Option getOption() {
        return Option.fromString(optionName);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "OptionSetEvent{" +
                "optionName='" + optionName + '\'' +
                ", value=" + value +
                '}';
    }
}

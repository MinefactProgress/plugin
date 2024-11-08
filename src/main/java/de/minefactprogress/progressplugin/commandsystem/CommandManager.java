/*
 * The MIT License (MIT)
 *
 *  Copyright © 2021, Alps BTE <bte.atchli@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.minefactprogress.progressplugin.commandsystem;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.commands.bannercreator.CMD_Banner;
import de.minefactprogress.progressplugin.commandsystem.commands.locationeditor.CMD_LocationEditor;
import de.minefactprogress.progressplugin.commandsystem.commands.noclip.CMD_NoClip;
import de.minefactprogress.progressplugin.commandsystem.commands.progress.CMD_Progress;
import de.minefactprogress.progressplugin.commandsystem.commands.worldedit.CMD_Side;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class CommandManager {

    public List<BaseCommand> baseCommands = new ArrayList<>() {{
        add(new CMD_Progress());
        add(new CMD_Banner());
        add(new CMD_NoClip());
//        add(new CMD_LocationEditor());
        add(new CMD_Side());
    }};

    public void init() {
        for(BaseCommand cmd : baseCommands) {
            for(String name : cmd.getNames()) {
                Objects.requireNonNull(Main.getInstance().getCommand(name)).setExecutor(cmd);
                Objects.requireNonNull(Main.getInstance().getCommand(name)).setTabCompleter(cmd);
            }
        }
    }
}

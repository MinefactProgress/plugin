package de.minefactprogress.progressplugin.entities.city;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BlockCount {
    private int total;
    private int done;
    private int left;
}

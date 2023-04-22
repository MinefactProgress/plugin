package de.minefactprogress.progressplugin.utils;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import org.bukkit.Location;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtils {

    public static District getCurrentDistrict(Location loc) {
        HashMap<Integer, ArrayList<Point2D.Double>> areas = convertDistrictToAreas(
                API.getDistricts().stream().filter(d -> d.getId() != 1).toList()
        );

        double[] playerPos = CoordinateConversion.convertToGeo(loc.getX(), loc.getZ());
        for (Map.Entry<Integer, ArrayList<Point2D.Double>> entry : areas.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            if (Utils.inside(new Point2D.Double(playerPos[0], playerPos[1]), entry.getValue())) {
                return District.getDistrictById(entry.getKey());
            }
        }
        return District.getDistrictById(1);
    }

    public static Block getCurrentBlock(Location loc) {
        District district = getCurrentDistrict(loc);

        if (district.getId() != 1) {
            HashMap<Integer, ArrayList<Point2D.Double>> areas = convertBlockToAreas(
                    API.getBlocks().stream().filter(b -> b.getDistrictId() == district.getId()).toList()
            );

            double[] playerPos = CoordinateConversion.convertToGeo(loc.getX(), loc.getZ());
            for (Map.Entry<Integer, ArrayList<Point2D.Double>> entry : areas.entrySet()) {
                if (entry.getValue().isEmpty()) continue;
                if (Utils.inside(new Point2D.Double(playerPos[0], playerPos[1]), entry.getValue())) {
                    return Block.getBlock(district, entry.getKey());
                }
            }
        }
        return null;
    }

    private static HashMap<Integer, ArrayList<Point2D.Double>> convertDistrictToAreas(List<District> object) {
        HashMap<Integer,ArrayList<Point2D.Double>> result = new HashMap<>();
        for(District e : object) {
            result.put(e.getId(), e.getArea());
        }
        return result;
    }

    private static HashMap<Integer, ArrayList<Point2D.Double>> convertBlockToAreas(List<Block> object) {
        HashMap<Integer,ArrayList<Point2D.Double>> result = new HashMap<>();
        for(Block e : object) {
            result.put(e.getId(), e.getArea());
        }
        return result;
    }
}

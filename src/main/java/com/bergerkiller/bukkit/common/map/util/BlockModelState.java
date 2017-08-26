package com.bergerkiller.bukkit.common.map.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * A block can be in various states (facing, color, type, etc.).
 * When found, a BlockModelState gives information about this.
 */
public class BlockModelState {
    public List<Multipart> multipart;

    // Note: is VariantList because 'normal' (normal distribution) keys use randomness
    // to select a random variant at render time. We always return only the first variant.
    public Map<Condition, VariantList> variants;

    /**
     * Finds all variants that are displayed based on Block data options
     * 
     * @param options of the block
     * @return variants that are displayed
     */
    public VariantList findVariants(Map<String, String> options) {
        VariantList result = new VariantList();
        if (this.multipart != null) {
            for (Multipart part : multipart) {
                if (part.when.has(options)) {
                    result.addAll(part.apply);
                }
            }
        } else if (this.variants != null) {
            for (Map.Entry<Condition, VariantList> entry : variants.entrySet()) {
                if (entry.getKey().has(options) && !entry.getValue().isEmpty()) {
                    result.add(entry.getValue().get(0));
                }
            }
        }
        return result;
    }

    public static class Multipart {
        public Condition when = Condition.ALWAYS;
        public VariantList apply;
    }

    public static class Variant {
        @SerializedName("model")
        public String modelName;

        @SerializedName("x")
        public float rotationX = 0.0f;

        @SerializedName("y")
        public float rotationY = 0.0f;

        @SerializedName("z")
        public float rotationZ = 0.0f;

        public boolean uvlock = false;
    }

    public static class VariantList extends ArrayList<Variant> {
        private static final long serialVersionUID = 1L;
    }

    /**
     * A single condition node in a condition tree hierarchy
     */
    public static class Condition {
        public String key = null;
        public String value = null;
        public Mode mode = Mode.SELF;
        public List<Condition> conditions;

        public static final Condition ALWAYS = new Condition() {
            @Override
            public boolean has(Map<String, String> options) {
                return true;
            }
        };

        public boolean has(Map<String, String> options) {
            if (this.mode == Mode.AND) {
                // AND mode
                if (this.conditions == null || this.conditions.isEmpty()) {
                    return false;
                }
                for (Condition condition : this.conditions) {
                    if (!condition.has(options)) {
                        return false;
                    }
                }
                return true;
            } else if (this.mode == Mode.OR) {
                // OR mode
                if (this.conditions == null || this.conditions.isEmpty()) {
                    return false;
                }
                for (Condition condition : this.conditions) {
                    if (condition.has(options)) {
                        return true;
                    }
                }
                return false;
            } else {
                // 'normal' key is for normal distribution only
                // This one always passes
                if (this.key.equals("normal") && this.value.equals("")) {
                    return true;
                }

                // Check own key-value pair
                String option = options.get(this.key);
                return option != null && option.equals(this.value);
            }
        }
        
        public enum Mode {
            SELF, AND, OR
        }
    }
}
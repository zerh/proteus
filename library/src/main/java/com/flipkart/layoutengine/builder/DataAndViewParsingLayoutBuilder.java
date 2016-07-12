package com.flipkart.layoutengine.builder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.flipkart.layoutengine.toolbox.IdGenerator;
import com.flipkart.layoutengine.toolbox.Styles;
import com.flipkart.layoutengine.toolbox.Utils;
import com.flipkart.layoutengine.view.ProteusView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * A layout builder which can parse @data and @view blocks before passing it on to
 * {@link SimpleLayoutBuilder}
 */
public class DataAndViewParsingLayoutBuilder extends DataParsingLayoutBuilder {

    private Map<String, JsonObject> layouts;

    protected DataAndViewParsingLayoutBuilder(Map<String, JsonObject> layouts, @NonNull IdGenerator idGenerator) {
        super(idGenerator);
        this.layouts = layouts;
    }

    @Override
    protected ProteusView onUnknownViewEncountered(String type, View parent, JsonObject source, JsonObject data, int index, Styles styles) {
        JsonElement element = null;
        if (layouts != null) {
            element = layouts.get(type);
        }
        if (element != null && !element.isJsonNull()) {
            JsonObject layout = element.getAsJsonObject();
            layout = Utils.mergeLayouts(layout, source);
            ProteusView view = build(parent, layout, data, index, styles);
            onViewBuiltFromViewProvider(view, type, parent, index);
            return view;
        }
        return super.onUnknownViewEncountered(type, parent, source, data, index, styles);
    }

    private void onViewBuiltFromViewProvider(ProteusView view, String type, View parent, int childIndex) {
        if (listener != null) {
            listener.onViewBuiltFromViewProvider(view, parent, type, childIndex);
        }
    }
}

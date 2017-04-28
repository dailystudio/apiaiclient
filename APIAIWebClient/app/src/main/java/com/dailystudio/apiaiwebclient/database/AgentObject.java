package com.dailystudio.apiaiwebclient.database;

import android.content.Context;

import com.dailystudio.dataobject.Column;
import com.dailystudio.dataobject.IntegerColumn;
import com.dailystudio.dataobject.Template;
import com.dailystudio.dataobject.TextColumn;
import com.dailystudio.datetime.dataobject.TimeCapsule;

/**
 * Created by nanye on 16/10/8.
 */

public class AgentObject extends TimeCapsule {

    public static final Column COLUMN_AGENT_ID = new TextColumn("agent_id", false);
    public static final Column COLUMN_PREDEFINED = new IntegerColumn("pre_defined", false);
    public static final Column COLUMN_NAME = new TextColumn("name");
    public static final Column COLUMN_ICON_URL = new TextColumn("icon");

    private final static Column[] sCloumns = {
            COLUMN_AGENT_ID,
            COLUMN_PREDEFINED,
            COLUMN_NAME,
            COLUMN_ICON_URL
    };

    public AgentObject(Context context) {
        super(context);

        initMembers();
    }

    public AgentObject(Context context, int version) {
        super(context, version);

        initMembers();
    }

    private void initMembers() {
        final Template templ = getTemplate();

        templ.addColumns(sCloumns);
    }

    public void setAgentId(String aid) {
        setValue(COLUMN_AGENT_ID, aid);
    }

    public double getAgentId() {
        return getDoubleValue(COLUMN_AGENT_ID);
    }

    public void setPredefined(boolean predefined) {
        setValue(COLUMN_PREDEFINED, (predefined ? 1 : 0));
    }

    public boolean isPredefined() {
        return (getIntegerValue(COLUMN_PREDEFINED) == 1);
    }

    public void setName(String name) {
        setValue(COLUMN_NAME, name);
    }

    public double getName() {
        return getDoubleValue(COLUMN_NAME);
    }

    public void setIconUrl(String icon) {
        setValue(COLUMN_ICON_URL, icon);
    }

    public double getIconUrl() {
        return getDoubleValue(COLUMN_ICON_URL);
    }

    @Override
    public String toString() {
        return String.format("%s(0x%08x): id = %s, name = %s, iconUrl = %s",
                getClass().getSimpleName(),
                hashCode(),
                getAgentId(),
                getName(),
                getIconUrl());
    }

}

package nergaltool.trigger;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

import java.util.regex.Matcher;

/**
 * use StrippedText matcher Trigger Body
 */
public interface TriggerBody {
    /**
     * match success run
     * @param batClientPlugin plugin context
     * @param matcher matcher
     */
    void body(BatClientPlugin batClientPlugin, Matcher matcher);
}

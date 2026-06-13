import arc.Events;
import arc.util.Strings;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.mod.Plugin;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CustomPanel extends Plugin {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("HH:mm dd.MM");
    private static String mapName = "";
    private static Timer.Task timerTask;
    @Override
    public void init() {
        Events.on(EventType.PlayEvent.class, e -> { mapName = Vars.state.map == null ? "" : Strings.stripColors(Vars.state.map.name()); update(); });
        Events.on(EventType.PlayerJoin.class, e -> update());
        if (timerTask != null) timerTask.cancel();
        timerTask = Timer.schedule(CustomPanel::update, (60f - LocalDateTime.now().getSecond()) % 60f + 0.1f, 60f);
    }
    private static void update() {
        if (mapName.isEmpty() || Vars.state.is(GameState.State.menu)) return;
        Vars.state.rules.mission = "[#dadada]Map:\n" + mapName + "\nTime: " + LocalDateTime.now().format(F);
        if (!Groups.player.isEmpty()) Call.setRules(Vars.state.rules);
    }
}

package github.AqumpusAXY.appliedenoughitems.util;

import appeng.client.gui.me.common.Repo;

public class RepoManager {
    private static Repo currentRepo;

    public static void setCurrentRepo(Repo repo) {
        currentRepo = repo;
    }

    public static Repo getCurrentRepo() {
        return currentRepo;
    }
}

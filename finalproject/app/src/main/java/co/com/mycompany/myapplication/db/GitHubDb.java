package co.com.mycompany.myapplication.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import co.com.mycompany.myapplication.model.Contributor;
import co.com.mycompany.myapplication.model.Repo;
import co.com.mycompany.myapplication.model.RepoSearchResult;
import co.com.mycompany.myapplication.model.User;

/**
 * Se define la base de datos con los entities y los daos
 */
@Database(entities = {User.class, Repo.class, Contributor.class, RepoSearchResult.class}, version = 1)
public abstract class GitHubDb extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract RepoDao repoDao();
}

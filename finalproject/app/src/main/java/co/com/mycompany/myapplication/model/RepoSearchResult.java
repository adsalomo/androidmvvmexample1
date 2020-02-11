package co.com.mycompany.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.List;

import co.com.mycompany.myapplication.db.GitHubTypeConverters;

@Entity(primaryKeys = {"query"})
@TypeConverters(GitHubTypeConverters.class)
public class RepoSearchResult {
    @NonNull
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCount;
    public final Integer next;

    public RepoSearchResult(@NonNull String query, List<Integer> repoIds, int totalCount, Integer next) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}

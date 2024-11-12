package com.zholtikov.cloud_dove.dal;

import com.zholtikov.cloud_dove.enums.SortState;
import com.zholtikov.cloud_dove.exception.ValidationExceptionCustom;
import com.zholtikov.cloud_dove.model.PictureMeta;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PictureRepository {
    private final JdbcTemplate jdbcTemplate;

    public PictureMeta create(PictureMeta picture) throws ValidationException {

        String sql = "insert into clouddove.pictures(owner_id, file_name, file_size, uploaded) " +
                "values(?,?,?,?) ON CONFLICT DO NOTHING";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"picture_id"});
            stmt.setLong(1, picture.getOwnerId());
            stmt.setString(2, picture.getFileName());
            stmt.setLong(3, picture.getFileSize());
            stmt.setTimestamp(4, Timestamp.valueOf(picture.getUploaded()));
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findPictureMetaById(key);
    }

    public PictureMeta findPictureMetaById(Long id) {

        final String sql = "select * from clouddove.pictures where PICTURE_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToPictureMeta, id);
    }

    public void checkOwnershipByPictureName(Long ownerId, String fileName) {
        final String sql = "select COUNT(picture_id) from clouddove.pictures where owner_id = ?" +
                " AND file_name = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, ownerId, fileName);
        if (count == null || count == 0) {
            throw new ValidationExceptionCustom("Picture doesn't belong to user " + ownerId + ", or file name is incorrect.");
        }
    }

    public Long getFileSize(Long ownerId, String filename) {
        final String sql = "select file_size from clouddove.pictures where owner_id = ?" +
                " AND file_name = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, ownerId, filename);

    }

    public List<PictureMeta> findPictureMetasForOwner(Long ownerId, LocalDateTime dateFrom, LocalDateTime dateTo,
                                                      Long sizeFrom, Long sizeTo, Long picIdFrom, Long picIdTo, SortState sortState) {
        StringBuilder builder = new StringBuilder();
        builder.append(" where owner_id = ").append(ownerId);
        if (dateFrom != null) builder.append(" AND uploaded >= '").append(dateFrom).append("'");
        if (dateTo != null) builder.append(" AND uploaded <= '").append(dateTo).append("'");
        if (sizeFrom != null) builder.append(" AND file_size >= ").append(sizeFrom);
        if (sizeTo != null) builder.append(" AND file_size <= ").append(sizeTo);
        if (picIdFrom != null) builder.append(" AND picture_id >= ").append(picIdFrom);
        if (picIdTo != null) builder.append(" AND picture_id <= ").append(picIdTo);

        if (sortState != null) {
            switch (sortState) {
                case NONE -> {
                }
                case DATE_SORT_ASC -> builder.append(" ORDER BY uploaded ASC");
                case DATE_SORT_DESC -> builder.append(" ORDER BY uploaded DESC");
                case ID_SORT_ASC -> builder.append(" ORDER BY picture_id ASC");
                case ID_SORT_DESC -> builder.append(" ORDER BY picture_id DESC");
                case SIZE_SORT_ASC -> builder.append(" ORDER BY file_size ASC");
                case SIZE_SORT_DESC -> builder.append(" ORDER BY file_size DESC");
                default -> throw new ValidationExceptionCustom("Unknown state of sorting: " + sortState);
            }
        }

        String whereState = builder.toString();
        final String sql = "select * from clouddove.pictures" + whereState;

        return jdbcTemplate.query(sql, this::mapRowToPictureMeta);
    }

    public List<PictureMeta> findPictureMetasForModerator(LocalDateTime dateFrom, LocalDateTime dateTo,
                                                          Long sizeFrom, Long sizeTo, Long picIdFrom, Long picIdTo, SortState sortState) {
        boolean hasFirstClause = false;
        StringBuilder builder = new StringBuilder();
        if (dateFrom != null) {
            builder.append(" WHERE");
            hasFirstClause = true;
            builder.append(" uploaded >= '").append(dateFrom).append("'");
        }
        if (dateTo != null) {
            if (hasFirstClause) {
                builder.append(" AND");
            } else {
                builder.append(" WHERE");
                hasFirstClause = true;
            }
            builder.append(" uploaded <= '").append(dateTo).append("'");
        }
        if (sizeFrom != null) {
            if (hasFirstClause) {
                builder.append(" AND");
            } else {
                builder.append(" WHERE");
                hasFirstClause = true;
            }
            builder.append(" file_size >= ").append(sizeFrom);
        }
        if (sizeTo != null) {
            if (hasFirstClause) {
                builder.append(" AND");
            } else {
                builder.append(" WHERE");
                hasFirstClause = true;
            }
            builder.append(" file_size <= ").append(sizeTo);
        }
        if (picIdFrom != null) {
            if (hasFirstClause) {
                builder.append(" AND");
            } else {
                builder.append(" WHERE");
                hasFirstClause = true;
            }
            builder.append(" picture_id >= ").append(picIdFrom);
        }
        if (picIdTo != null) {
            if (hasFirstClause) {
                builder.append(" AND");
            } else {
                builder.append(" WHERE");
                hasFirstClause = true;
            }
            builder.append(" picture_id <= ").append(picIdTo);
        }

        if (sortState != null) {
            switch (sortState) {
                case NONE -> {
                }
                case DATE_SORT_ASC -> builder.append(" ORDER BY uploaded ASC");
                case DATE_SORT_DESC -> builder.append(" ORDER BY uploaded DESC");
                case ID_SORT_ASC -> builder.append(" ORDER BY picture_id ASC");
                case ID_SORT_DESC -> builder.append(" ORDER BY picture_id DESC");
                case SIZE_SORT_ASC -> builder.append(" ORDER BY file_size ASC");
                case SIZE_SORT_DESC -> builder.append(" ORDER BY file_size DESC");
                default -> throw new ValidationExceptionCustom("Unknown state of sorting: " + sortState);
            }
        }

        String whereState = builder.toString();
        final String sql = "select * from clouddove.pictures" + whereState;

        return jdbcTemplate.query(sql, this::mapRowToPictureMeta);
    }


    private PictureMeta mapRowToPictureMeta(ResultSet resultSet, int rowNum) throws SQLException {
        return PictureMeta.builder()
                .id(resultSet.getLong("PICTURE_ID"))
                .ownerId(resultSet.getLong("owner_id"))
                .fileName(resultSet.getString("file_name"))
                .fileSize(resultSet.getLong("file_size"))
                .uploaded(resultSet.getTimestamp("uploaded").toLocalDateTime())
                .build();
    }
}

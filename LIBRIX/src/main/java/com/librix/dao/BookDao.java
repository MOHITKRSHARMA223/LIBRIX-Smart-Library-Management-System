package com.librix.dao;

import com.librix.config.ConnectionFactory;
import com.librix.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class BookDao {
    private final ConnectionFactory connectionFactory;
    public BookDao(ConnectionFactory connectionFactory) { this.connectionFactory = connectionFactory; }

    public List<Book> search(String query, int limit, int offset) throws SQLException {
        String sql = "SELECT id, isbn, title, a.name, c.name, total_copies, available_copies FROM books b JOIN authors a ON a.id=b.author_id LEFT JOIN categories c ON c.id=b.category_id WHERE LOWER(b.title) LIKE ? OR LOWER(a.name) LIKE ? ORDER BY b.title LIMIT ? OFFSET ?";
        String pattern = "%" + query.trim().toLowerCase() + "%";
        List<Book> books = new ArrayList<>();
        try (Connection connection = connectionFactory.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern); statement.setString(2, pattern); statement.setInt(3, limit); statement.setInt(4, offset);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) books.add(new Book(results.getLong(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getInt(6), results.getInt(7)));
            }
        }
        return books;
    }
}

package domain.ip.authorization;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthRequestRepositoryInMemory implements AuthRequestRepository {
    private final List<AuthRequest> table = new ArrayList<>(
            List.of(AuthRequest.ofDefault())
    );

    @Override
    public AuthRequest first() {
        return table.get(0);
    }

    @Override
    public List<AuthRequest> all() {
        return table;
    }

    @Override
    public void add(AuthRequest request) {
        table.add(request);
    }
}

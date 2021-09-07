package domain.ip.authorization;

import java.util.List;

public interface AuthRequestRepository {
    AuthRequest first();

    List<AuthRequest> all();

    void add(AuthRequest request);
}

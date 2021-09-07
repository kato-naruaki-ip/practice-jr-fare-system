package domain.ip.authorization;

import domain.ip.user.User;
import domain.ip.util.TimeStamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class AuthRequest {
    @Getter
    @NonNull
    AuthRequestId id;

    @Getter
    @NonNull
    User requester = User.ofDefault();

    @Getter
    @NonNull
    List<Assignee> assignees = new ArrayList<>(List.of(Assignee.ofDefault()));

    @Getter(lazy = true)
    AuthStatus status = computeStatus();

    private AuthStatus computeStatus() {
        return assignees.stream()
                .allMatch(assignee -> assignee.getStatus().equals(AuthStatus.AUTHORIZED))
                ? AuthStatus.AUTHORIZED
                : AuthStatus.UNDER_AUTHORIZATION;
    }

    @Getter
    @NonNull
    TimeStamp due = TimeStamp.ofDefault();

    @Getter(lazy = true)
    TimeStamp authorizedAt = computeAuthorizedAt();

    private TimeStamp computeAuthorizedAt() {
        // TODO
        return TimeStamp.ofDefault();
    }

    @NonNull
    RequestStatus requestStatus;

    static AuthRequest ofDefault() {
        return AuthRequest.builder()
                .id(1L)
                .build();
    }

    public static AuthRequestBuilder builder() {
        return new AuthRequestBuilder();
    }

    @ToString
    public static class AuthRequestBuilder {
        private AuthRequestId id;
        private RequestStatus requestStatus = RequestStatus.INACTIVE;

        AuthRequestBuilder() {
        }

        public AuthRequestBuilder id(long id) {
            this.id = new AuthRequestId(id);
            return this;
        }

        public AuthRequestBuilder isDeleted(boolean isDeleted) {
            this.requestStatus = isDeleted ? RequestStatus.INACTIVE : RequestStatus.ACTIVE;
            return this;
        }

        public AuthRequest build() {
            return new AuthRequest(id, requestStatus);
        }
    }
}

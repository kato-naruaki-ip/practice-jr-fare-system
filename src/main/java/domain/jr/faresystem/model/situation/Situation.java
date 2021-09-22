package domain.jr.faresystem.model.situation;

import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Situation {
    Map<Direction, Party> parties;

    public Optional<Party> getParty(Direction direction) {
        return Optional.ofNullable(parties.get(direction));
    }

    public boolean is_片道() {
        return getParty(Direction._ゆき).isPresent() && getParty(Direction._かえり).isEmpty();
    }

    public boolean is_往復() {
        return getParty(Direction._ゆき).isPresent() && getParty(Direction._かえり).isPresent();
    }

    public enum Direction {
        _ゆき, _かえり
    }

    private Situation(SituationBuilder builder) {
        this.parties = Collections.unmodifiableMap(builder.parties);
    }

    @ToString
    public static class SituationBuilder {
        private final Map<Direction, Party> parties;

        public SituationBuilder() {
            this.parties = new EnumMap<>(Direction.class);
        }

        public SituationBuilder _ゆき(Party party) {
            parties.put(Direction._ゆき, party);
            return this;
        }

        public SituationBuilder _かえり(Party party) {
            parties.put(Direction._かえり, party);
            return this;
        }

        public Situation build() {
            if (parties.get(Direction._ゆき) == null) {
                throw new IllegalStateException();
            }

            return new Situation(this);
        }
    }
}

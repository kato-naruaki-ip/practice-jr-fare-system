package domain.jr.faresystem.model.situation;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.externalsystems.util.time.Date;
import domain.jr.faresystem.model.client.Client;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@Builder(toBuilder = true, buildMethodName = "prebuild")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Party {
    @Getter
    Station departure;

    @Getter
    Station destination;

    @Getter
    @Singular
    List<Client> clients;

    @Getter
    Date date;

    @Getter
    SuperExpress superExpress;

    @Builder.Default
    boolean isFreeSeat = false;

    public boolean isFreeSeat() {
        return isFreeSeat;
    }

    public static void validate(Party party) {
        Objects.requireNonNull(party.departure, "departure");
        Objects.requireNonNull(party.destination, "destination");
        Objects.requireNonNull(party.date, "date");
        Objects.requireNonNull(party.superExpress, "superExpress");

        Objects.requireNonNull(party.clients, "clients");
        for (Client client : party.clients)
            Objects.requireNonNull(client, "client");
        if (party.clients.size() == 0)
            throw new IllegalArgumentException("clients.size() should be positive");
    }

    public static PartyBuilder builder(
            Station departure,
            Station destination,
            Date date,
            SuperExpress superExpress
    ) {
        return new PartyBuilder()
                .departure(departure)
                .destination(destination)
                .date(date)
                .superExpress(superExpress);
    }

    public static final class PartyBuilder {
        private PartyBuilder() {
        }

        public PartyBuilder switchDepartureAndDestination() {
            var tmp = departure;
            departure = destination;
            destination = tmp;
            return this;
        }

        public PartyBuilder addClients(Collection<? extends Client> clients) {
            if (this.clients == null) this.clients = new ArrayList<>();

            this.clients.addAll(clients);
            return this;
        }

        public PartyBuilder addClientRepeatedly(Client client, int count) {
            if (this.clients == null) this.clients = new ArrayList<>();

            this.clients.addAll(
                    IntStream.range(0, count).mapToObj(n -> client).collect(Collectors.toList())
            );
            return this;
        }

        public Party build() {
            var party = this.prebuild();

            validate(party);

            return party;
        }
    }
}

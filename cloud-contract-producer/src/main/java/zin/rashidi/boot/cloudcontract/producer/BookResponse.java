package zin.rashidi.boot.cloudcontract.producer;

import java.util.UUID;

public record BookResponse(UUID id, String title, String author) {
}

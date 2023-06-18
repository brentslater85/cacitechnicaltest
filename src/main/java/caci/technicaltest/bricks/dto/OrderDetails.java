package caci.technicaltest.bricks.dto;

public record OrderDetails(String orderReference, int quantity) {}

// this probably now needs updating to include the status in the return.
// The spec doesn't ask for that so I haven't done it but in real life i would suggest that we do

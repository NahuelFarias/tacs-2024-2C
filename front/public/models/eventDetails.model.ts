export class EventDetailModel{
    id: number;
    name: string;
    location: string;
    date: Date;
    admin: string;
    zones: Array<ZoneModel>;
}

export class ZoneModel{
    zoneLocation: string;
    availableTickets: number;
}
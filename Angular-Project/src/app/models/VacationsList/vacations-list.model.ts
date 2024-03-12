export class VacationsList {
  id: number = 0;
  title: string = '';
  destination: string = '';
  location: string = ''
  poisNumber: number = 0;
  src: string = '';
  season: string = '';

  constructor(id: number, title: string, destination: string, location: string, poisNumber: number, src: string, season: string) {
    this.id = id;
    this.title = title;
    this.destination = destination;
    this.poisNumber = poisNumber;
    this.src = src;
    this.location = location;
    this.season = season;
  }
}


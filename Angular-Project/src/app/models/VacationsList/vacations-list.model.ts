export class VacationsList {
  id: number = 0;
  title: string = '';
  destination: string = '';
  poisNumber: number = 0;
  src: string = '';
  distance: number = 0;

  constructor(id: number, title: string, destination: string, poisNumber: number, src: string, distance: number) {
    this.id = id;
    this.title = title;
    this.destination = destination;
    this.poisNumber = poisNumber;
    this.src = src;
    this.distance = distance;
  }
}


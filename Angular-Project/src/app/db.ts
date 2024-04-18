import Dexie, {Table} from 'dexie';

export interface Users {
  id?: number;
  username: string;
  location: string;
  yearOfBirth: number | null;
  gender: string | null;
  email: string;
  password: string;
  src: string;
}

export interface PointOfInterest {
  name: string;
  photo: string;
  description: string;
  longitude: number;
  latitude: number;
  tags: string[];
  stars: number;
  address: string;
  phone: string;
  website: string;
  schedule: string[];
}

export interface Itineraries {
  id?: number;
  userId: number;
  photo: string;
  name: string;
  destination: string;
  startingPoint: string;
  season: string | null;
  attendant: string | null;
  transport: string | null;
  budget: string | null;
  distance: number;
  poisNumber: number;
  highlights: string[];
  pois: PointOfInterest[];
}

export class AppDB extends Dexie {
  users!: Table<Users, number>;
  itineraries!: Table<Itineraries, number>;

  constructor() {
    super('RelationTripDataBase');
    this.version(3).stores({
      users: '++id, username, email',
      itineraries: '++id',
    });
  }
}

export const db = new AppDB();

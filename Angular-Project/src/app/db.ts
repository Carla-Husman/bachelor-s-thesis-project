import Dexie, { Table } from 'dexie';

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

export class AppDB extends Dexie {
  users!: Table<Users, number>;

  constructor() {
    super('RelationTripDataBase');
    this.version(3).stores({
      users: '++id, username, email',
    });
  }
}

export const db = new AppDB();

import { Sport } from 'app/entities/enumerations/sport.model';

export interface ITypeSport {
  id?: number;
  sport?: Sport | null;
}

export class TypeSport implements ITypeSport {
  constructor(public id?: number, public sport?: Sport | null) {}
}

export function getTypeSportIdentifier(typeSport: ITypeSport): number | undefined {
  return typeSport.id;
}

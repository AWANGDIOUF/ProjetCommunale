import { IEquipe } from 'app/entities/equipe/equipe.model';
import { IClub } from 'app/entities/club/club.model';
import { Sport } from 'app/entities/enumerations/sport.model';

export interface ITypeSport {
  id?: number;
  sport?: Sport | null;
  equipe?: IEquipe | null;
  club?: IClub | null;
}

export class TypeSport implements ITypeSport {
  constructor(public id?: number, public sport?: Sport | null, public equipe?: IEquipe | null, public club?: IClub | null) {}
}

export function getTypeSportIdentifier(typeSport: ITypeSport): number | undefined {
  return typeSport.id;
}

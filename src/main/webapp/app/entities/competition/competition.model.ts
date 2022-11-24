import * as dayjs from 'dayjs';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { IClub } from 'app/entities/club/club.model';
import { DisciplineClub } from 'app/entities/enumerations/discipline-club.model';

export interface ICompetition {
  id?: number;
  date?: dayjs.Dayjs | null;
  lieu?: string | null;
  discipline?: DisciplineClub | null;
  vainqueur?: IVainqueur | null;
  clubs?: IClub[] | null;
}

export class Competition implements ICompetition {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public lieu?: string | null,
    public discipline?: DisciplineClub | null,
    public vainqueur?: IVainqueur | null,
    public clubs?: IClub[] | null
  ) {}
}

export function getCompetitionIdentifier(competition: ICompetition): number | undefined {
  return competition.id;
}

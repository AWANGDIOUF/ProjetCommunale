import dayjs from 'dayjs/esm';
import { IVainqueur } from 'app/entities/vainqueur/vainqueur.model';
import { IClub } from 'app/entities/club/club.model';
import { DisciplineClub } from 'app/entities/enumerations/discipline-club.model';

export interface ICompetition {
  id?: number;
  dateCompetition?: dayjs.Dayjs | null;
  lieuCompetition?: string | null;
  discipline?: DisciplineClub | null;
  vainqueurs?: IVainqueur[] | null;
  club?: IClub | null;
}

export class Competition implements ICompetition {
  constructor(
    public id?: number,
    public dateCompetition?: dayjs.Dayjs | null,
    public lieuCompetition?: string | null,
    public discipline?: DisciplineClub | null,
    public vainqueurs?: IVainqueur[] | null,
    public club?: IClub | null
  ) {}
}

export function getCompetitionIdentifier(competition: ICompetition): number | undefined {
  return competition.id;
}

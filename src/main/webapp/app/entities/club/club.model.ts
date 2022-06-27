import dayjs from 'dayjs/esm';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { ICombattant } from 'app/entities/combattant/combattant.model';
import { ICompetition } from 'app/entities/competition/competition.model';
import { IArchiveSport } from 'app/entities/archive-sport/archive-sport.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { DisciplineClub } from 'app/entities/enumerations/discipline-club.model';

export interface IClub {
  id?: number;
  nomClub?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  logoContentType?: string | null;
  logo?: string | null;
  discipline?: DisciplineClub | null;
  typeSport?: ITypeSport | null;
  combattants?: ICombattant[] | null;
  competitions?: ICompetition[] | null;
  archves?: IArchiveSport[] | null;
  quartier?: IQuartier | null;
}

export class Club implements IClub {
  constructor(
    public id?: number,
    public nomClub?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public discipline?: DisciplineClub | null,
    public typeSport?: ITypeSport | null,
    public combattants?: ICombattant[] | null,
    public competitions?: ICompetition[] | null,
    public archves?: IArchiveSport[] | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getClubIdentifier(club: IClub): number | undefined {
  return club.id;
}

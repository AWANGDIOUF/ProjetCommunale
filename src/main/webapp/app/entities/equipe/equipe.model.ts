import dayjs from 'dayjs/esm';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { IJoueur } from 'app/entities/joueur/joueur.model';
import { IMatch } from 'app/entities/match/match.model';
import { IArchiveSport } from 'app/entities/archive-sport/archive-sport.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';

export interface IEquipe {
  id?: number;
  nomEquipe?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  logoContentType?: string | null;
  logo?: string | null;
  typeSport?: ITypeSport | null;
  joueurs?: IJoueur[] | null;
  matches?: IMatch[] | null;
  archves?: IArchiveSport[] | null;
  quartier?: IQuartier | null;
}

export class Equipe implements IEquipe {
  constructor(
    public id?: number,
    public nomEquipe?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public typeSport?: ITypeSport | null,
    public joueurs?: IJoueur[] | null,
    public matches?: IMatch[] | null,
    public archves?: IArchiveSport[] | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getEquipeIdentifier(equipe: IEquipe): number | undefined {
  return equipe.id;
}

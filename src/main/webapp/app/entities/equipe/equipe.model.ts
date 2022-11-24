import * as dayjs from 'dayjs';
import { ITypeSport } from 'app/entities/type-sport/type-sport.model';
import { IJoueur } from 'app/entities/joueur/joueur.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { IMatch } from 'app/entities/match/match.model';
import { IArchiveSport } from 'app/entities/archive-sport/archive-sport.model';

export interface IEquipe {
  id?: number;
  nomEquipe?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  logoContentType?: string | null;
  logo?: string | null;
  typeSport?: ITypeSport | null;
  joueur?: IJoueur | null;
  quartiers?: IQuartier[] | null;
  matches?: IMatch[] | null;
  archves?: IArchiveSport[] | null;
}

export class Equipe implements IEquipe {
  constructor(
    public id?: number,
    public nomEquipe?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public typeSport?: ITypeSport | null,
    public joueur?: IJoueur | null,
    public quartiers?: IQuartier[] | null,
    public matches?: IMatch[] | null,
    public archves?: IArchiveSport[] | null
  ) {}
}

export function getEquipeIdentifier(equipe: IEquipe): number | undefined {
  return equipe.id;
}

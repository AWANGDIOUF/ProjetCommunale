import dayjs from 'dayjs/esm';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { Poste } from 'app/entities/enumerations/poste.model';

export interface IJoueur {
  id?: number;
  nomJoueur?: string | null;
  prenomJoueur?: string | null;
  dateNaisJoueur?: dayjs.Dayjs | null;
  lieuNaisJoueur?: string | null;
  posteJoueur?: Poste | null;
  photoJoueurContentType?: string | null;
  photoJoueur?: string | null;
  equipe?: IEquipe | null;
}

export class Joueur implements IJoueur {
  constructor(
    public id?: number,
    public nomJoueur?: string | null,
    public prenomJoueur?: string | null,
    public dateNaisJoueur?: dayjs.Dayjs | null,
    public lieuNaisJoueur?: string | null,
    public posteJoueur?: Poste | null,
    public photoJoueurContentType?: string | null,
    public photoJoueur?: string | null,
    public equipe?: IEquipe | null
  ) {}
}

export function getJoueurIdentifier(joueur: IJoueur): number | undefined {
  return joueur.id;
}

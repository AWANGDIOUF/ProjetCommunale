import * as dayjs from 'dayjs';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { Poste } from 'app/entities/enumerations/poste.model';

export interface IJoueur {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  dateNais?: dayjs.Dayjs | null;
  lieuNais?: string | null;
  poste?: Poste | null;
  photoContentType?: string | null;
  photo?: string | null;
  equipes?: IEquipe[] | null;
}

export class Joueur implements IJoueur {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public dateNais?: dayjs.Dayjs | null,
    public lieuNais?: string | null,
    public poste?: Poste | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public equipes?: IEquipe[] | null
  ) {}
}

export function getJoueurIdentifier(joueur: IJoueur): number | undefined {
  return joueur.id;
}

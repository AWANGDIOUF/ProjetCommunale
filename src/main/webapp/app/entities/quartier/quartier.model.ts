import { IEquipe } from 'app/entities/equipe/equipe.model';
import { IClub } from 'app/entities/club/club.model';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';

export interface IQuartier {
  id?: number;
  nomQuartier?: string | null;
  equipe?: IEquipe | null;
  club?: IClub | null;
  beneficiaire?: IBeneficiaire | null;
}

export class Quartier implements IQuartier {
  constructor(
    public id?: number,
    public nomQuartier?: string | null,
    public equipe?: IEquipe | null,
    public club?: IClub | null,
    public beneficiaire?: IBeneficiaire | null
  ) {}
}

export function getQuartierIdentifier(quartier: IQuartier): number | undefined {
  return quartier.id;
}

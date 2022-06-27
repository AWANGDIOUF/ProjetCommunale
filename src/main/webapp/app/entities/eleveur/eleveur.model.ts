import { IQuartier } from 'app/entities/quartier/quartier.model';
import { TypeElevage } from 'app/entities/enumerations/type-elevage.model';

export interface IEleveur {
  id?: number;
  nomEleveur?: string | null;
  prenomEleveur?: string | null;
  telEleveur?: string | null;
  tel1Eleveur?: string | null;
  adresse?: string | null;
  nomElevage?: TypeElevage | null;
  descriptionActivite?: string | null;
  quartier?: IQuartier | null;
}

export class Eleveur implements IEleveur {
  constructor(
    public id?: number,
    public nomEleveur?: string | null,
    public prenomEleveur?: string | null,
    public telEleveur?: string | null,
    public tel1Eleveur?: string | null,
    public adresse?: string | null,
    public nomElevage?: TypeElevage | null,
    public descriptionActivite?: string | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getEleveurIdentifier(eleveur: IEleveur): number | undefined {
  return eleveur.id;
}

import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { TypeBeneficiaire } from 'app/entities/enumerations/type-beneficiaire.model';

export interface IBeneficiaire {
  id?: number;
  typeBeneficiaire?: TypeBeneficiaire | null;
  autreBeneficiaire?: string | null;
  description?: string | null;
  justification?: string | null;
  annonces?: IAnnonce[] | null;
  quartier?: IQuartier | null;
}

export class Beneficiaire implements IBeneficiaire {
  constructor(
    public id?: number,
    public typeBeneficiaire?: TypeBeneficiaire | null,
    public autreBeneficiaire?: string | null,
    public description?: string | null,
    public justification?: string | null,
    public annonces?: IAnnonce[] | null,
    public quartier?: IQuartier | null
  ) {}
}

export function getBeneficiaireIdentifier(beneficiaire: IBeneficiaire): number | undefined {
  return beneficiaire.id;
}

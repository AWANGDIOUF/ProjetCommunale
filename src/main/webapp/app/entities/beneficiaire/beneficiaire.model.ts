import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { TypeBeneficiaire } from 'app/entities/enumerations/type-beneficiaire.model';
import { TypeMoral } from 'app/entities/enumerations/type-moral.model';

export interface IBeneficiaire {
  id?: number;
  typeBenefiaire?: TypeBeneficiaire | null;
  typePersoMoral?: TypeMoral | null;
  prenom?: string | null;
  nom?: string | null;
  cin?: string | null;
  adresse?: string | null;
  tel1?: string;
  autretel1?: string | null;
  emailAssociation?: string | null;
  nomPresident?: string | null;
  description?: string | null;
  annonce?: IAnnonce | null;
  quartiers?: IQuartier[] | null;
}

export class Beneficiaire implements IBeneficiaire {
  constructor(
    public id?: number,
    public typeBenefiaire?: TypeBeneficiaire | null,
    public typePersoMoral?: TypeMoral | null,
    public prenom?: string | null,
    public nom?: string | null,
    public cin?: string | null,
    public adresse?: string | null,
    public tel1?: string,
    public autretel1?: string | null,
    public emailAssociation?: string | null,
    public nomPresident?: string | null,
    public description?: string | null,
    public annonce?: IAnnonce | null,
    public quartiers?: IQuartier[] | null
  ) {}
}

export function getBeneficiaireIdentifier(beneficiaire: IBeneficiaire): number | undefined {
  return beneficiaire.id;
}

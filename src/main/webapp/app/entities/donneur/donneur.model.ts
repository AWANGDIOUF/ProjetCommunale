import { IDon } from 'app/entities/don/don.model';
import { IDonSang } from 'app/entities/don-sang/don-sang.model';
import { TypeDonneur } from 'app/entities/enumerations/type-donneur.model';

export interface IDonneur {
  id?: number;
  typeDonneur?: TypeDonneur | null;
  prenom?: string | null;
  nom?: string;
  email?: string | null;
  adresse?: string | null;
  tel1?: string | null;
  ville?: string | null;
  description?: string | null;
  dons?: IDon[] | null;
  donSangs?: IDonSang[] | null;
}

export class Donneur implements IDonneur {
  constructor(
    public id?: number,
    public typeDonneur?: TypeDonneur | null,
    public prenom?: string | null,
    public nom?: string,
    public email?: string | null,
    public adresse?: string | null,
    public tel1?: string | null,
    public ville?: string | null,
    public description?: string | null,
    public dons?: IDon[] | null,
    public donSangs?: IDonSang[] | null
  ) {}
}

export function getDonneurIdentifier(donneur: IDonneur): number | undefined {
  return donneur.id;
}

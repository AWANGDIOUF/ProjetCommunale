import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IDonneur } from 'app/entities/donneur/donneur.model';
import { TypeDon } from 'app/entities/enumerations/type-don.model';

export interface IDon {
  id?: number;
  typeDon?: TypeDon | null;
  montant?: number | null;
  description?: string | null;
  annonce?: IAnnonce | null;
  donneurs?: IDonneur[] | null;
}

export class Don implements IDon {
  constructor(
    public id?: number,
    public typeDon?: TypeDon | null,
    public montant?: number | null,
    public description?: string | null,
    public annonce?: IAnnonce | null,
    public donneurs?: IDonneur[] | null
  ) {}
}

export function getDonIdentifier(don: IDon): number | undefined {
  return don.id;
}

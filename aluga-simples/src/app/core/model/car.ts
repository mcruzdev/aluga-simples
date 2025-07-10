export interface CarModel {
  id: number
  model: string
  brand: string
  carTitle: string
  year: number
  status: CarStatus,
  engine: string
}

export enum CarStatus {
  DISPONIVEL,
  ALUGADO,
  EM_MANUTENCAO
}

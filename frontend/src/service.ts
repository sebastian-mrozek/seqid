import axios from "axios";
import type { NumericSequence } from "./model";

interface SequenceService {
  getAll(onResolve: (data: NumericSequence[]) => void): void;
}

export const sequenceService: SequenceService = ((): SequenceService => {
  const sequenceService = axios.create({ baseURL: "http://localhost:7000/sequence" });

  function getAll(onResolve: (data: NumericSequence[]) => void) {
    sequenceService
      .get<NumericSequence[]>("")
      .then((response) => {
        console.log(response);
        onResolve(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  return {
    getAll,
  };
})();

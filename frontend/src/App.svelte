<script lang="ts">
  import SequenceListView from "./components/SequenceListView.svelte";
  import NewSequenceEditor from "./components/NewSequenceEditor.svelte";
  import type { NumericSequence, NumericSequenceDefinition } from "./model";
  import { sequenceService } from "./service";

  let sequences: NumericSequence[] = [];

  function fetchSequences() {
    sequenceService.getAll((data) => (sequences = data));
    console.log("request issued");
  }

  function createNew(event) {
    sequenceService.create(event.detail, (newSequence) => (sequences = [...sequences, newSequence]));
  }

  fetchSequences();
</script>

<main class="main">
  Sequences
  <button on:click={fetchSequences}>Refresh</button>
  <NewSequenceEditor on:create={createNew} />
  <SequenceListView {sequences} />
</main>

<style>
  .main {
    margin: 1em;
  }
</style>

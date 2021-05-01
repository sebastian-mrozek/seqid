<script lang="ts">
  import SequenceListView from "./components/SequenceListView.svelte";
  import type { NumericSequence } from "./model";
  import { sequenceService } from "./service";

  let sequences: NumericSequence[] = [];

  function fetchSequences() {
    sequenceService.getAll((data) => (sequences = data));
    console.log("request issued");
  }

  fetchSequences();

  // let sequences: NumericSequence[] = [
  //   {
  //     id: "aa",
  //     sequenceDefinition: {
  //       name: "some sequence",
  //       namespace: "ns1",
  //       start: 1,
  //     },
  //     lastValue: 5,
  //   },
  //   {
  //     id: "ab",
  //     sequenceDefinition: {
  //       name: "another sequence",
  //       namespace: "ns2",
  //       start: 2,
  //     },
  //     lastValue: 5115,
  //   },
  // ];
</script>

<main class="main">
  Sequences <button on:click={fetchSequences}>Refresh</button>
  <SequenceListView {sequences} />
</main>

<style>
  .main {
    margin: 1em;
  }
</style>

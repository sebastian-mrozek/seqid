<script lang="ts">
  import type { NumericSequence } from "../model";
  import SequenceView from "./SequenceView.svelte";

  export let sequences: NumericSequence[];
</script>

<div class="seq-list">
  {#each sequences as sequence}
    <div class="seq-item"><SequenceView {sequence} /></div>
  {/each}
</div>

<style>
  .seq-list {
    margin: 2em;
    border: 1px solid gray;
    border-radius: 3px;
  }
  .seq-item {
    margin: 0;
    padding: 0.2em;
    border-bottom: 1px solid gray;
  }
  .seq-item:last-child {
    margin: 0;
    padding: 0.2em;
    border-bottom: 0px;
  }
</style>
